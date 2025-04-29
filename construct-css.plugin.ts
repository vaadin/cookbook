import cssnanoPlugin from 'cssnano';
import { readFile } from 'node:fs/promises';
import postcss from 'postcss';
import type { Plugin } from 'vite';

const cssTransformer = postcss([cssnanoPlugin()]);

export async function compileCSS(contents: string, from: string): Promise<string> {
  const compiled = await cssTransformer
    .process(contents, { from })
    .then(({ content: c }) => c.replaceAll(/[`$]/gmu, '\\$&'));

  return `const css = new CSSStyleSheet();css.replaceSync(\`${compiled}\`);export default css;`;
}

// This plugin transforms CSS to Constructible CSSStyleSheet
export function constructCss(): Plugin {
  const css = new Map();

  return {
    enforce: 'post',
    name: 'vite-construct-css',
    async load(id) {
      if (id.endsWith('.css?construct')) {
        const content = await readFile(id, 'utf8');
        css.set(id, content);
        return {
          code: '',
        };
      }

      return null;
    },
    async transform(_, id) {
      if (id.endsWith('.css?construct')) {
        return {
          code: await compileCSS(css.get(id), id),
        };
      }

      return null;
    },
  };
}
