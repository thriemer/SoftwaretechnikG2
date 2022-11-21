const HtmlWebpackPlugin = require("html-webpack-plugin");
const MonacoWebpackPlugin = require("monaco-editor-webpack-plugin");
const path = require("path");

module.exports = {
  mode: "none",
  entry: "./src/code-editor.ts",
  output: {
    path: path.resolve(__dirname, "dist"),
    filename: '[name].bundle.js'
  },
    module: {
      rules: [
        {
          test: /\.m?js$/,
          exclude: /(node_modules|bower_components)/,
          use: {
            loader: "babel-loader",
            options: {
              presets: [
                "@babel/preset-env"
              ],
            },
          },
        },
      {
        test: /\.tsx?$/,
        use: 'ts-loader',
        exclude: /node_modules/,
      },
      			{
      				test: /\.css$/,
      				use: ['style-loader', 'css-loader']
      			},
      			{
      				test: /\.ttf$/,
      				use: ['file-loader']
      			}
      ],
    },
    plugins: [
    new HtmlWebpackPlugin({
      filename: "index.html",
      template: "./src/index.html",
    }),
        new MonacoWebpackPlugin({
        			languages: ['typescript', 'javascript', 'css']
        		})
  ],
};
