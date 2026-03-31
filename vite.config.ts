import { UserConfigFn } from 'vite';
import { overrideVaadinConfig } from './vite.generated';
import { execSync } from 'child_process'
  ;
const customConfig: UserConfigFn = (env) => ({
  // Here you can add custom Vite parameters
  // https://vitejs.dev/config/
});

export default overrideVaadinConfig(customConfig);

const run = (cmd:string) => {
  console.log(`Running ${cmd}`);
  try {
    execSync(cmd, { encoding: 'utf-8', stdio: 'inherit' });
  } catch (error:any) {
    // Do not fail if this was just skipped
    if (error.status != 133) {
      throw error;
    }
  }
};
run('node --experimental-strip-types parseClientRoutes.ts "src/main/frontend/recipe/**/*.ts"');
