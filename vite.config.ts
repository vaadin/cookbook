import { UserConfigFn } from 'vite';
import { overrideVaadinConfig } from './vite.generated';
const { execSync } = require('child_process');

const customConfig: UserConfigFn = (env) => ({
  // Here you can add custom Vite parameters
  // https://vitejs.dev/config/
});

export default overrideVaadinConfig(customConfig);


const run = (cmd) => {
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
run('parseClientRoutes');
