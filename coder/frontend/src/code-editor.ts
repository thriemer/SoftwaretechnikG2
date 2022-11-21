import {LitElement, html} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

@customElement('code-editor')
export class MyElement extends LitElement {
  @property()
  width = 800;
  @property()
  height = 600;
  @property()
  language = 'java';
  @property()
  code = 'public class Main{public static void main(String args[]){System.out.println("Hello World");}}';
    createRenderRoot() {
        return this;
    }
  render() {
    return html`
      <div id="editor" style="width:${this.width}px; height:600px; border:1px solid #ccc;"></div>
    <p>${this.code}</p>
    `;
  }

    firstUpdated() {
      monaco.editor.create(document.getElementById('editor')!, {
      	value: this.code,
      	language: this.language,
      	theme: 'vs-dark'
      });
    }

}
