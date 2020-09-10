/**
 * This file has been autogenerated as it didn't exist or was made for an older
 * incompatible version.
 *
 * This file can be used for manual configuration. It will not be modified
 * if the flowDefaults constant exists.
 */
const merge = require("webpack-merge");
const flowDefaults = require("./webpack.generated.js");
const path = require("path");

const { spawn } = require("child_process");

/**
 * To change the webpack config, add a new configuration object in
 * the merge arguments below:
 */
module.exports = merge(flowDefaults, {
  plugins: [
    function (compiler) {
      compiler.hooks.beforeCompile.tapAsync(
        "GenerateRouteInfo",
        (compilation, done) => {
          const parseClientRoutes = spawn(process.execPath, [
            path.resolve(__dirname, "parseClientRoutes.js"),
            path.resolve(__dirname, "frontend/recipe/**/*.ts"),
          ]);

          parseClientRoutes.stdout.on("data", (data) => {
            console.log(data.toString("utf-8"));
          });

          parseClientRoutes.on("close", (code) => {
            if (code !== 0) {
              throw `parseClientRoutes process exited with code ${code}`;
            }
            done();
          });
        }
      );
    },
  ],
});
