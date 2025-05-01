import { TabSheet } from '@vaadin/react-components/TabSheet.js';
import { Tabs } from '@vaadin/react-components/Tabs.js';
import { Tab } from '@vaadin/react-components/Tab.js';
import { useLoad } from 'Frontend/utils/load.js';
import React, { useEffect, type PropsWithChildren, Suspense } from 'react';
import Prism from 'prismjs';
import 'prismjs/components/prism-java';
import 'prismjs/components/prism-typescript';
import 'prismjs/themes/prism-okaidia.css';
import css from './code-viewer.module.css';

function removeMetadataTag(code: string): string {
  const METADATA_LENGTH = '@Metadata'.length;
  const startIdx = code.indexOf('@Metadata');
  let endIdx = -1;
  let openBrackets = 0;
  for (let i = startIdx + METADATA_LENGTH; i < code.length; i++) {
    if (code.charAt(i) === '(') {
      ++openBrackets;
    } else if (code.charAt(i) === ')') {
      --openBrackets;
      if (openBrackets === 0) {
        endIdx = code.substring(i).indexOf('\n') + i + 1;
        break;
      }
    }
  }
  return code.replace(code.slice(startIdx, endIdx), '');
}

function removeMetaImports(code: string): string {
  return code
    .split('\n')
    .filter((line) => !line.includes('import com.vaadin.recipes.recipe'))
    .join('\n');
}

function removeMetaInfoFromCode(code: string, language: string): string {
  if (!code) {
    return '';
  }
  code = code.substring(code.search(/^import/gm)); // remove package
  if (language === 'java') {
    code = code.replace('extends Recipe', 'extends VerticalLayout');
    if (!code.includes('import com.vaadin.flow.component.orderedlayout.VerticalLayout')) {
      code = 'import com.vaadin.flow.component.orderedlayout.VerticalLayout\n' + code;
    }
  }
  code = removeMetadataTag(code);
  code = removeMetaImports(code);
  return code;
}

type CodeSnippetProps = Readonly<{ file: string }>;

type PrismWrapperProps = Readonly<{ language: string }>;

function PrismWrapper({ children, language }: PropsWithChildren<PrismWrapperProps>) {
  const ref = React.useRef<HTMLPreElement>(null);

  useEffect(() => {
    if (ref.current != null) {
      Prism.highlightAllUnder(ref.current);
    }
  }, [ref.current]);

  return (
    <pre ref={ref}>
      <code className={`language-${language}`}>{children}</code>
    </pre>
  );
}

function CodeSnippet({ file }: PropsWithChildren<CodeSnippetProps>) {
  const FileContent = useLoad(async () => {
    const response = await fetch(`/api/source/${file}`);
    let code = await response.text();
    code = removeMetaInfoFromCode(code, language);
    code = code
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
    return code;
  }, []);

  const language = file.substring(file.lastIndexOf('.') + 1);

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <PrismWrapper language={language}>
        <FileContent />
      </PrismWrapper>
    </Suspense>
  );
}

type CodeViewerProps = Readonly<{
  className?: string;
  files: readonly string[];
}>;

function CodeViewer({ className, files }: CodeViewerProps) {
  return (
    <TabSheet className={`${css.host} ${className}`}>
      <Tabs theme="cookbook-code">
        {files.map((file) => (
          <Tab key={file} id={file} theme="cookbook-code">
            {file.substring(file.lastIndexOf('/') + 1)}
          </Tab>
        ))}
      </Tabs>
      {files.map((file) => (
        <CodeSnippet key={file} file={file} />
      ))}
    </TabSheet>
  );
}

export default CodeViewer;
