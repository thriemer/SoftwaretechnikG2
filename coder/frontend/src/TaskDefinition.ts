interface Task{
    name: string;
    taskDescription: string;
    codeTemplate: string;
}
interface TestResult{
    correct: boolean,
    message: string
}
export{Task, TestResult}