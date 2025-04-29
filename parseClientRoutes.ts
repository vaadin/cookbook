import { globIterate as glob } from 'glob';
import { readFile, writeFile } from 'node:fs/promises';
import { basename, dirname, extname, join, resolve } from 'node:path';
import { relative } from 'node:path/posix';
import { fileURLToPath } from 'node:url';
import { parseArgs } from 'node:util';
import ast from 'tsc-template';
import {
  createPrinter,
  createSourceFile,
  factory,
  forEachChild,
  isArrayLiteralExpression,
  isCallExpression,
  isDecorator,
  isIdentifier,
  isObjectLiteralExpression,
  isPropertyAccessExpression,
  isPropertyAssignment,
  NewLineKind,
  type Node,
  type ObjectLiteralExpression,
  type PropertyAccessExpression,
  ScriptTarget,
  type SourceFile,
  transform,
  visitEachChild,
} from 'typescript';

const {
  positionals: [filesGlob],
} = parseArgs({
  allowPositionals: true,
});

const printer = createPrinter({ newLine: NewLineKind.LineFeed });

function extractMetadata(source: SourceFile): ObjectLiteralExpression | undefined {
  function find(node: Node): ObjectLiteralExpression | undefined {
    if (
      isDecorator(node) &&
      isCallExpression(node.expression) &&
      isIdentifier(node.expression.expression) &&
      node.expression.expression.text == 'recipeInfo'
    ) {
      const [metadata] = node.expression.arguments;

      if (!isObjectLiteralExpression(metadata)) {
        throw new Error('Expected object literal expression as an argument of @recipeInfo');
      }

      return metadata;
    }

    return forEachChild(node, find);
  }

  return find(source);
}

const tags = ['Tag.TYPE_SCRIPT', 'Tag.HILLA'];

function mapRecipeToViewConfig(obj: ObjectLiteralExpression, source: SourceFile): ObjectLiteralExpression {
  const defaultTags = tags.map((tag) => ast`${tag}`.node as PropertyAccessExpression);

  let howDoI: string | undefined;
  let hasTags = false;
  function visit(node: Node) {
    if (isPropertyAssignment(node) && isIdentifier(node.name)) {
      switch (node.name.text) {
        case 'url':
          return undefined;
        case 'howDoI':
          howDoI = node.initializer.getText(source);
          return undefined;
        case 'tags': {
          hasTags = true;

          if (!isArrayLiteralExpression(node.initializer)) {
            throw new Error('Expected array literal expression for tags property');
          }

          return factory.updatePropertyAssignment(
            node,
            node.name,
            factory.createArrayLiteralExpression([
              ...defaultTags,
              ...node.initializer.elements.filter(
                (element) => !(isPropertyAccessExpression(element) && tags.includes(element.getText(source))),
              ),
            ]),
          );
        }
        default:
          break;
      }
    }

    return node;
  }

  let detail = transform(obj, [(context) => (rootNode) => visitEachChild(rootNode, visit, context)]).transformed[0];

  if (!hasTags) {
    detail = factory.updateObjectLiteralExpression(detail, [
      ...detail.properties,
      factory.createPropertyAssignment('tags', factory.createArrayLiteralExpression(defaultTags)),
    ]);
  }

  return ast`var dummy = %{ {
    ${howDoI ? `title: ${howDoI},` : ''}
    detail: ${detail},
  } }%;`.node as ObjectLiteralExpression;
}

console.debug(`Looking for ${filesGlob}...`);

const cwd = fileURLToPath(new URL('./', import.meta.url));
const frontend = join(cwd, 'frontend');
const views = join(frontend, 'views');

for await (const file of glob(filesGlob, { cwd })) {
  const filePath = join(cwd, file);
  const fileName = basename(file, extname(file));
  console.debug(`Scanning: ${fileName}`);
  const code = await readFile(filePath, 'utf8');
  const source = createSourceFile('my-view.ts', code, ScriptTarget.Latest);

  const metadata = extractMetadata(source);

  if (!metadata) {
    console.debug(`No metadata found for ${fileName}. Skipping...`);
    continue;
  }

  const viewConfigObject = mapRecipeToViewConfig(metadata, source);

  // Transform kebab-case -> PascalCase
  const reactComponentName = fileName.replace(/(^\w|-\w)/gu, (str) => str.replace(/-/u, '').toUpperCase());
  const routePath = resolve(views, `${fileName}.tsx`);

  const route = ast`
    import '${relative(dirname(routePath), `${filePath.substring(0, filePath.length - extname(filePath).length)}.js`)}';
    import Tag from '../generated/com/vaadin/recipes/recipe/Tag.js';
    import type { ViewConfig } from './_shared/ViewConfig.js';
  
    export const config: ViewConfig = ${viewConfigObject};
    
    export default function ${reactComponentName}() {
      return <${fileName} />;
    }
  `.source;

  await writeFile(resolve(views, routePath), printer.printFile(route), 'utf8');
  console.debug(`Created: ${routePath}`);
}
