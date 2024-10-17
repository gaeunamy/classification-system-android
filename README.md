# 실시간 과일 분류 시스템 애플리케이션
<br/>

## 📝 작품소개
YOLOv5를 이용한 과일 분류 시스템을 통해 자동으로 사과를 분류하고, 전용 앱을 통해 실시간으로 통계와 이미지를 확인할 수 있는 기능을 제공하고자 하였습니다.
<br/>
<br/>

## 🌁 프로젝트 배경
기존의 수작업 품질 검사 방식은 시간과 인력 소모가 크고, 환경 조건에 따라 정확도가 떨어지는 문제점이 있었습니다. 
이러한 문제를 해결하기 위해 다양한 조명과 각도에서도 높은 정확도로 사과의 외형적 결함을 감지하고 분류할 수 있는 YOLOv5 기반의 과일 분류 시스템을 개발하였습니다.
농업 종사자가 언제 어디서나 분류 결과를 실시간으로 확인하고 품질 관리 작업을 수행할 수 있도록, 앱을 통해 분류 결과에 따른 통계와 이미지를 제공하고자 합니다.
<br/>

## 🎞 Demo  


https://github.com/user-attachments/assets/c9e87c14-217e-42b2-b99c-264e4906792c



<br/>

# ⭐ 주요 기능

- **메인화면**: 통계, 전체 데이터, 정상 데이터, 불량 데이터로 구성

<table>
  <tr>
    <td align="center" width="50%">
      <img src="https://github.com/user-attachments/assets/575e07b7-aceb-4565-993a-a0b871bf5663" width="75%" />
    </td>
    <td align="center" width="50%">
      <img src="https://github.com/user-attachments/assets/20810cb2-9d5b-4f35-ba2c-bef329078bc1" width="75%" />
    </td>
  </tr>
  <tr>
    <td align="center" style="border: none;">
      <p align="center">인트로 화면</p>
    </td>
    <td align="center" style="border: none;">
      <p align="center">메인 화면</p>
    </td>
  </tr>
</table>
<br/>

- **상태별 사과 통계**: 분류 작업을 거친 전체/정상/불량 사과의 통계를 그래프로 제공하며, 날짜별 통계 제공

<table>
  <tr>
    <td align="center" width="50%">
      <img src="https://github.com/user-attachments/assets/543853c6-f063-4930-9cd6-7221351414c1" width="75%" />
    </td>
    <td align="center" width="50%">
      <img src="https://github.com/user-attachments/assets/598ad5d9-d7ad-49bc-a2b0-1ef306ba3412" width="75%" />
    </td>
  </tr>
  <tr>
    <td align="center" style="border: none;">
      <p align="center">전체 통계</p>
    </td>
    <td align="center" style="border: none;">
      <p align="center">일별 통계</p>
    </td>
  </tr>
</table>
<br/>

- **분류 데이터 확인**
  - 전체 데이터: DB에 저장된 형식과 동일하게 데이터 개수, 파일명, 저장된 날짜/시간 표시

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/24406ff2-3cf9-4f21-b6b9-b415b2d477ec" width="75%" />
    </td>
  </tr>
  <tr>
    <td align="center" style="border: none;">
      <p align="center">전체 데이터</p>
    </td>
  </tr>
</table>
<br/>

   - 정상/불량 데이터: 각각 정상/불량 데이터를 이미지와 파일명으로 표시. 각 항목 선택 시 큰 이미지 확인 가능
<table>
  <tr>
    <td align="center" width="50%">
      <img src="https://github.com/user-attachments/assets/3ab95163-a4a8-4781-9cec-530f30851189" width="75%" />
    </td>
    <td align="center" width="50%">
      <img src="https://github.com/user-attachments/assets/de099f6c-eb7c-478b-87f1-d89a423578f9" width="75%" />
    </td>
  </tr>
  <tr>
    <td align="center" style="border: none;">
      <p align="center">불량 데이터</p>
    </td>
    <td align="center" style="border: none;">
      <p align="center">항목 선택 시</p>
    </td>
  </tr>
</table>
<br/>

## 🔧 Stack
**Frontend**  
- **Language**: Java, XML  
- **Tools**: Android Studio(Iguana | 2023.2.1 Patch 1)  
- **Deploy**: Firebase (Storage, Real-Time Database)  
<br/>

## 💡 기대효과
- **자동화**: 사과 분류 작업을 자동화하여 시간과 인건비 절감
- **실시간 관리**: 언제 어디서든 앱을 통해 실시간으로 분류된 사과 상태와 통계를 확인 가능
- **정확한 데이터 분석**: 이미지 판별 정확도와 일별 통계를 통해 데이터 기반의 품질 관리가 가능
- **확장 가능성**: 다른 과일이나 채소로 시스템 확장이 가능하며 농업 분야의 다양한 자동화 적용 가능성

<br/>

## 🙋‍♂️ Developer
|  Fullstack |             
| :--------: | 
| [김가은](https://github.com/gaeunamy) |
