import {LitElement,css, html} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import { createRef, Ref, ref } from "lit/directives/ref.js";
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

@customElement('code-editor')
export class CodeEditor extends LitElement {
  private container: Ref<HTMLElement> = createRef();
  editor?: monaco.editor.IStandaloneCodeEditor;
  @property()
  width = 800;
  @property()
  height = 600;
  @property()
  language? : string;
  @property()
  code? : string;
    createRenderRoot() {
        return this;
    }
  render() {
    return html`
      <main ${ref(this.container)}  style="width:${this.width}px; height:600px; border:1px solid #ccc;"></main>
    `;
  }

    firstUpdated() {
     this.editor= monaco.editor.create(this.container.value!, {
      	value: this.code,
      	language: this.language,
      	theme: 'vs-dark'
      });
      this.editor.onDidChangeModelContent((e)=> {
      this.code=this.editor!.getValue();
      });
    }

updated(changedProperties: Map<string, unknown>) {
    if(changedProperties.has('language')){
        monaco.editor.setModelLanguage(this.editor!.getModel()!, this.language!);
    }
}

}
