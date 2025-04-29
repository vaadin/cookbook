import { type UserConfigFn, type Plugin } from 'vite';
import { constructCss } from './construct-css.plugin.js';
import { overrideVaadinConfig } from './vite.generated';
import { execSync } from 'child_process';

const customConfig: UserConfigFn = (env) => ({
  // Here you can add custom Vite parameters
  // https://vitejs.dev/config/
  plugins: [constructCss()],
});

export default overrideVaadinConfig(customConfig);

const run = (cmd: string) => {
  const npmrun = `npm run ${cmd}`;
  console.log(`Running ${npmrun}`);
  try {
    console.log(execSync(npmrun, { encoding: 'utf-8', stdio: 'inherit' }));
  } catch (error) {
    // Do not fail if this was just skipped
    if (error.status != 133) {
      throw error;
    }
  }
};
// TODO: make this work, seems that needs
// run('parseClientRoutes');
