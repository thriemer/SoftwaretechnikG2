require.config({
    paths: {
        'vs': 'https://unpkg.com/monaco-editor@latest/min/vs'
    }
});
window.MonacoEnvironment = {
    getWorkerUrl: () => proxy
};

let proxy = URL.createObjectURL(new Blob([`
	self.MonacoEnvironment = {
		baseUrl: 'https://unpkg.com/monaco-editor@latest/min/'
	};
	importScripts('https://unpkg.com/monaco-editor@latest/min/vs/base/worker/workerMain.js');
`], {
    type: 'text/javascript'
}));
let params = new URLSearchParams(location.search.substring(1));
const response = await fetch("../api/task/listAll");
var taskToSolve;
await response.json().then(taskList => {
        for (const t of taskList) {
            if (params.get('task') === t.name) {
                taskToSolve = t;
                break;
            }
        }
    })
    .catch(ex => {
        console.log(ex);
    });

document.getElementById('taskName').innerHTML = taskToSolve.name;
document.getElementById('taskDescription').innerHTML = taskToSolve.taskDescription;

var editor;
require(["vs/editor/editor.main"], function() {
    editor = monaco.editor.create(document.getElementById('monacoEditor'), {
        value: taskToSolve.codeTemplate,
        language: params.get('language'),
        theme: 'vs-light'
    });
});

document.getElementById("submit").onclick = async () => {
            const response = await fetch("../api/task/submit?taskName=" + taskToSolve.name, {
                method: 'POST',
                body: editor.getValue()
            });
            let evaluation;
            if (!response.ok) {
                evaluation = [{
                    correct: false,
                    message: await response.text()
                }];
            }else{
           await response.json().then(data => {
                evaluation = data;
            }).catch(ex => {
                console.log(ex);
            });
            }
            var builtHTML = "<ul>";
            for(const e of evaluation){
               let showColor = e.correct ? '#4A8246': 'darkred';
               builtHTML = builtHTML + '<li style="color:'+ showColor+'">'+e.message+'</li>'
            }
            builtHTML = builtHTML+"</ul>";
            document.getElementById('evaluation').innerHTML =builtHTML;
}