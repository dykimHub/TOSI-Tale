const fs = require('fs');
const { spawn } = require('child_process');

const tempFilePath = process.argv[2];

fs.readFile(tempFilePath, 'utf8', (err, data) => {
    if (err) {
        console.error('파일 읽기 오류:', err);
        return;
    }

    const inputText = data;

    // 파이썬 스크립트 실행 명령
    const pythonProcess = spawn('python', ['src/main/java/com/ssafy/tosi/taleDetail/morpheme/kss_split.py']);
    console.log("자바스크립트:" + inputText);

    // 입력 텍스트를 파이썬 스크립트로 전달
    pythonProcess.stdin.write(inputText);
    pythonProcess.stdin.end();

    // 응답 출력
    pythonProcess.stdout.on('data', (data) => {
        console.log(data.toString());
    });

    pythonProcess.stderr.on('data', (data) => {
        console.error(data.toString());
    });

    pythonProcess.on('close', (code) => {
        console.log(`파이썬 프로세스 종료 코드: ${code}`);
    });
});
