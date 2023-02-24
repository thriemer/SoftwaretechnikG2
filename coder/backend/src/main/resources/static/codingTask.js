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
const response = await fetch("../api/task/"+params.get('task'));
var taskToSolve;
var codeTemplate;
await response.json().then(taskAndSubmittedSolution => {
    taskToSolve = taskAndSubmittedSolution.task;
    showEvaluation(taskAndSubmittedSolution.evaluation);
    if(taskAndSubmittedSolution.submission==undefined){
        codeTemplate= taskToSolve.codeTemplate;
    }else{
        codeTemplate= taskAndSubmittedSolution.submission;
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
        value: codeTemplate,
        language: params.get('language'),
        theme: 'vs-light'
    });
});

function showEvaluation(evaluation){
var builtHTML = "";
if(evaluation == undefined){
    builtHTML = "Nothing to show!";
}else{
                var builtHTML = "<ul>";
                for(const e of evaluation){
                   let showColor = e.correct ? '#4A8246': 'darkred';
                   builtHTML = builtHTML + '<li style="color:'+ showColor+'">'+e.message.replace(/(?:\r\n|\r|\n)/g, '<br>')+'</li>'
                }
                builtHTML = builtHTML+"</ul>";
}
                document.getElementById('evaluation').innerHTML =builtHTML;
}

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
            showEvaluation(evaluation);
}