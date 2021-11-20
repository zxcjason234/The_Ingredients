# The_Ingredients
## 摘要
2019年第74屆聯合國大會上提出了將每年9月29日訂為「國際食材損失與浪費問題宣傳日」，因為每年有3分之1的食材、約13億噸的食材被白白浪費掉，而食材遭到損耗的原因，有超過50%是源自於不合胃口、擺到過期；因此本小組規劃一個可以減少食物浪費的系統，協助使用者在每次購物後清楚地記錄購買的食材，在食物過期前主動通知使用者，再依據使用者的行為特性，利用基於內容的推薦演算法(Content-based- filtering)，推薦最適合使用者冰箱現有食材的食譜，讓使用者可以在食材過期前，將食材轉變為可口的料理；相信透過提醒食材的到期日，並搭配客製化推薦滿足消費者喜歡的食譜，可以提高使用者對食物的料理興趣，以達到減少食材遭到浪費的情況。

開放資料資料集:
1. 政府資料開放平台
- 水產精品資料:https://data.gov.tw/dataset/6034
- 農漁會年度百大農業精品好禮:https://data.gov.tw/dataset/24657
- 南投縣農特商家介紹(水果館):https://data.gov.tw/dataset/95028
- 南投縣十大熱銷農產品:https://data.gov.tw/dataset/95030
- 南投縣農特產品介紹(水果館):https://data.gov.tw/dataset/95038
- 南投縣農特商家介紹(根莖蔬菜館):https://data.gov.tw/dataset/95042

2.	台北市政府資料平台
- 臺北市各類農產品批發市場歷史交易量、交易額、平均價表一覽:
https://data.taipei/#/dataset/detail?id=e8d4f1bb-164a-4f6d-9c21-f2754656d4e4

**關鍵字：智慧冰箱、糧食危機、條碼辨識、個人化提醒、演算法推薦**

## 創意描述
1.	食譜推薦
- 客製化食譜推薦，降低使用者不知道能烹煮什麼料理的困擾
使用者需要烹煮料理時，不知道能煮什麼一直都是困擾著大部分人的問題，所以本系統利用基於內容的推薦演算法，依據使用者的行為特性及我們自行收集的食譜庫，在使用者需要食譜的時候，給予最佳的推薦。

- 使用者採購推薦，減少規劃採買食材的所需時間
不知道可以採買哪些食材來製作料理是使用者常見的困擾，本系統針對使用者缺少的食材進行推薦，以省去規劃採買清單的時間，也能避免購買多餘的食材。

2.	提供使用者一個整合平台，滿足使用者需求
家人的飲食喜好都不相同，為避免在飲食上有溝通不清楚的部分，透過本系統許願池的功能，讓使用者可以將想要吃的料理，或想購買食材記錄在許願池中，讓家中負責採購的成員得知。

3.	利用手機掃描建檔，排除需另外購買設備的費用
市面上少數具智慧功能的冰箱，具備掃描鏡頭的功能，但因掃描鏡頭與冰箱是一體成形的，故使用者必須購買一整台冰箱才能享有管理食材的功能；故本系統採手機掃描建檔將物品資訊新增至本系統中冰箱內容物功能內，既方便又省去購買設備的費用。

4.	食品保存期限將至，系統主動提醒使用者食材即將過期通知
冰箱中時常有食材擺放到過期的狀況發生，為了避免類似的情形，本系統在食材到期前主動發出即將過期通知，並在冰箱內容物功能中顯示即將過期的警示，以讓使用者得知冰箱中有哪些食材是即將過期的。

5.	整合民間食譜網站的料理資訊，提升食譜多樣性
本小組取得民間食譜網站的授權，將食譜相關資訊彙整至食譜資料庫，提供豐富且簡單的料理給使用者參考。

6.	彙整政府資料開放平台的農業精品，提供使用者更好的食材選擇
本小組透過彙整得獎之農業精品，讓使用者在缺少食材時，能優先選擇優良的在地食材。

![image](https://user-images.githubusercontent.com/43669016/142720103-791a619c-8adc-4ac4-97e2-69ad47d101f8.png)

## 系統特色
1.	掃描辨識:
透過手機鏡頭辨識上方食材條碼辨識出食材資訊，讓使用者更方便的紀錄自己的食材。

2.	許願池:
使用者將欲交代事項和購物清單留於許願池中，以利家中採購的成員查看需要購買清單以及代辦事項。

3.	★適性化推薦:
存放在冰箱中的各式食材，透過本系統提供的採購分析模組，推薦符合需求的食譜以方便進行採買，以及透過冰箱現有食材來推薦可供烹煮的食譜，讓推薦的內容更貼近使用者的需求。

![image](https://user-images.githubusercontent.com/43669016/142720277-b6b99774-4861-4b2b-bfac-c9681f6651cd.png)
![image](https://user-images.githubusercontent.com/43669016/142720290-ad8716ad-73f0-4816-bf0a-557ef301e05a.png)


4.	即期品提醒:
為了讓使用者更好管理自身的冰箱，本系統設置提醒功能，透過使用者自行設置食材時間，讓使用者能夠透過手機即時且準確的管理食材是否過期。
5.	飲食偏好設定:
透過問卷設定功能讓使用者記錄自己不喜歡的食材或吃葷吃素，使本系統能夠更精準統計使用者行為，讓使用者在推薦食譜功能上有更棒的瀏覽體驗。

## 系統開發工具與技術
1.	硬體:
Windows系統桌上型電腦、Android手持裝置各一台。
2.	軟體:
Android Studio(7.0以上)、MySQL、Python、Java、PHP進行開發。

## 系統使用對象
使用對象為一般家庭，讓使用者能夠更有效率管理自己冰箱食材的狀態，針對食材設定過期日期給予適當的提醒和購買需求，並利用使用者許願池讓使用者更能了解需要購買事項，給予使用者更有效的管理冰箱食材。

## 結語
食物浪費是刻不容緩的問題，我們希望透過本系統讓使用者輕鬆養成不浪費食材的好習慣，減少食物遭到浪費的情形。未來期許能和各大生鮮賣場合作，讓使用者在採購清單上新增品項時，系統預先建立好購買訂單，使用者就能一鍵送出到附近賣場，提供更簡便的消費模式。
