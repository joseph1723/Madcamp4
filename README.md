# Madcamp4
거기 헬스를 사랑하는 당신! 혹시 다음 중 하나에 해당되진 않으십니까?

- 운동할 때 마다 이상한 곳에 근육통이 생긴다
- 내 자세가 맞는건지 모르겠다
- 내 옆사람이 하는 운동이 너무 궁금하다
- 그때 그 운동이 뭐였지… 가물가물할 때가 많다
- 근데 PT등록하기는 부담스럽고, 트레이너한테 물어보기도 부끄럽다

당신만의 휴대(?)가능하고(Portable), 객관적인(Objective) 보조자(Spotter), POS가 항상 당신과 함께할 것입니다!!

## Vision Model

### 1. Posture Evaluation Model

- Model은 다중분류모델로, 은 여러겹의 Dense Layer와 Batch Normalization, Dropout을 사용하여 구성하였습니다
- 학습 데이터는 AI hub의 크로스핏 동작 데이터를 사용했습니다
- Input Features는 영상의 진행도 (현재 프레임/전체 프레임), 사용자의 Landmark points의 좌표, 각 관절의 꺽인 각도를 사용하였습니다
- Output vector를 통해 사용자의 운동에서 생길 수 있는 문제점 중 비중을 차지하는 것을 찾아내었습니다
- Mediapipe를 통해 사용자의 운동 영상을 분석하고 Landmark points들의 정보를 얻은 뒤, 이를 사전 학습된 모델에 넣어 자세를 평가합니다
- 각 종류의 운동은 다 별개의 모델을 통해 평가됩니다


수많은 EDA의 흔적…
![newplot (7)](https://github.com/user-attachments/assets/0ee65c2e-9a7c-4ff5-9e3b-ebf99258ab46)
![newplot (6)](https://github.com/user-attachments/assets/17299c50-b88f-4482-913d-205976dc95bd)
![newplot (1)](https://github.com/user-attachments/assets/6ff5d525-71c7-4417-bdc5-9ca75a2f9c31)
![newplot (5)](https://github.com/user-attachments/assets/92d96e19-a105-401a-9457-d7edfa30bfcc)
![newplot (4)](https://github.com/user-attachments/assets/ba66111e-7c6b-431e-97d2-a5c30e7a55e9)

### 2. Exercise classification model

- Computer Vision 모델을 사용한 다중 분류 모델입니다
- OpenCV2 라이브러리를 사용하여 input Video의 프레임 정보를 얻어내고, 이를 적절한 전처리를 거쳐 가공하여 모델을 학습시킵니다
- Posture Evaluation 모델과 다르게 Mediapipe나 Openpose등의 라이브러리를 통해 정보를 얻어낸 뒤 학습시킨게 아닌, 프레임 정보를 그대로 넣어 학습시킵니다

## Tab1 - 운동 평가

- **Tab1**은 **Posture Evaluation**을 수행하는 탭입니다
- 사용자의 운동 영상을 받아 시간대별로 분석하여 결과를 도출합니다
- 도출된 결과는 Geminai를 이용하여 사용자가 이해할 수 있게 분석하여 설명합니다

https://github.com/user-attachments/assets/6c11977d-d0dc-43bc-a92a-b210e658bd1c

https://github.com/user-attachments/assets/5c8b7d91-1c8b-4e14-86f2-9511dd85d8f3

## Tab2 - 운동찾기

- **Tab2**는 Exercise classification model을 사용하여 피사체가 수행하고 있는 운동을 추측하는 탭입니다
- Exercise classification model은 Computer Vision 기술을 사용하여 학습되었습니다
    
    

https://github.com/user-attachments/assets/e8ca5b89-4c78-475d-87cd-028b69f7250b



## Tab3 - 헬키네이터

- **Tab3**는 헬키네이터, 사용자의 질문을 받고 찾고 있는 운동이 무엇인가를 추측해내는 탭입니다
- Bayes theorem을 기반으로 한 naive bayes classifier를 사용하였습니다
- 질문에 대해 사용자가 제공하는 답을 바탕으로 기존 데이터베이스에 있는 운동 중 어떤 운동을 사용자가 찾고있는지를 추측해냅니다

https://github.com/user-attachments/assets/e0c8a62b-c926-461d-9071-25351e8dc8f0



# 후기

### 조민주

- 비싼 장비로 다양한 모델을 공부하고 만들어볼 수 있어 좋았다.
- 일주일만에 모델 n개를 양산했다는게 믿기지 않는다
- Tensorflow Keras 쓰레기 아니 같은회사서비스끼리 호환이안되는건 도대체
- 폐기당한 수많은 체육선생님 일러스트에 애도를…(어떤 분이 매우 좋아하심)

### 김동근

- 환경설정이 진짜 너무너무너무힘들었습니다
- AI는 시간과의 싸움이라는걸 너무 뼈저리게 느꼈습니다
- 그래도 Vision으로 AI 모델을 만들고, 또 그게 돌아가는걸 볼 때 너무 재밌었습니다
- 조모양이 체육쌤 짤들을 따로 모을정도로 좋아하더라구요. 특히 탱크탑(?)짤을..
- 저희 테스트 데이터셋을 모으는데 협력해주신 모든 분들께 크나큰 감사를 드립니다
- And I also TF싫어
