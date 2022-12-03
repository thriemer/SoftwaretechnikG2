import {LitElement,css, html} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import { createRef, Ref, ref } from "lit/directives/ref.js";
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import {Task, TestResult} from './TaskDefinition'

@customElement('code-editor')
export class CodeEditor extends LitElement {
  private container: Ref<HTMLElement> = createRef();
  editor?: monaco.editor.IStandaloneCodeEditor;
  @property()
  width = 800;
  @property()
  height = 600;
  @property()
  language = "java";
  @property()
  code? : string;
  createRenderRoot() {
        return this;
  }

  private currentTask?: Task;
  @property()
  private evaluation?: TestResult[];

  render() {
    return html`
    <p>${this.currentTask===undefined?"":this.currentTask.taskDescription}</p>
      <main ${ref(this.container)}  style="width:${this.width}px; height:600px; border:1px solid #ccc;"></main>
      <button @click="${this.submit}">Submit solution</button>
      <ul>
      ${this.generateEvaluationList()}
      </ul>
    `;
  }

  generateEvaluationList(){
  if(this.evaluation===undefined){
    return html``;
  }
 return this.evaluation.map((result) => html`<li style="color: ${result.correct?'green':'red'}">${result.message}</li>`);
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
      this.initTask();
    }

    initTask = async()=>{
        const response = await fetch("../api/task/listAll");
        response.json().then(data =>  {
        console.log(data);
        var taskList = data as Task[];
        this.currentTask = taskList[Math.floor(Math.random()*taskList.length)];
        this.editor!.setValue(this.currentTask.codeTemplate);
        })
        .catch(ex=>{console.log(ex);});
    }

    submit = async()=>{
        const response = await fetch("../api/task/submit?taskName="+this.currentTask!.name, {
            method: 'POST',
            body: this.code
        });
        response.json().then(data=>{
        this.evaluation = data as TestResult[];
        console.log(this.evaluation);
        }).catch(ex=>{console.log(ex);});
    }

updated(changedProperties: Map<string, unknown>) {
    if(changedProperties.has('language')){
        monaco.editor.setModelLanguage(this.editor!.getModel()!, this.language!);
    }
}

}
