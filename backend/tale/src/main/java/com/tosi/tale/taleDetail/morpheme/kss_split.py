import sys
from kss import split_sentences

sys.stdin.reconfigure(encoding='utf-8')
sys.stdout.reconfigure(encoding='utf-8')

input_text = sys.stdin.read()
print("파이썬:"+input_text)
sentences = split_sentences(input_text)

# print로 응답 보내기
for sentence in sentences:
    print(sentence)


