# -*- coding: utf-8 -*-
"""
Created on Wed Apr  7 23:47:43 2021

@author: syuch
"""


from sqlalchemy import create_engine
import pandas as pd
import pyodbc
import pymysql



def rec(i):
    i=str(i)
    db_settings = {
        "host": "127.0.0.1",
        "port": 3306,
        "user": "root",
        "password": "",
        "db": "123",
        "charset": "utf8"
    }
    
    engine = create_engine("mysql+pymysql://{}:{}@{}/{}?charset={}".format('root', '', '127.0.0.1:3306', '123','utf8'))
    co = engine.connect()#创建连接
    
    
    conn = pymysql.connect(**db_settings)
    
    cursor = conn.cursor()
    
    command = "SELECT Dishes_id,title,M_or_V ,CONCAT(dishes_type,'.',food_main,'.',country_cusine,'.',M_or_V)AS 'genres' FROM `dishes`"
    
    food_df = pd.read_sql(command, con=conn)
    
    # i= str(input("請輸入會員id: "))
    command1 = "SELECT member_id,dishes.title,rating FROM `user_ratings` LEFT JOIN dishes ON user_ratings.Dishes_ID=dishes.Dishes_id WHERE member_id=\"{}\" ".format(i)
    user_rating = pd.read_sql(command1, con=conn)
    
    command2="SELECT dishes_ingredient.Dishes_id,GROUP_CONCAT(ingredient_type.Ingredient_type)as ingredient_type,group_concat(Ingredient_title) as Ingredient_title FROM ((dishes_ingredient LEFT JOIN dishes ON dishes.Dishes_id=dishes_ingredient.Dishes_id)LEFT JOIN ingredient on dishes_ingredient.Ingredient_ID=ingredient.Ingredient_ID)INNER JOIN ingredient_type ON ingredient.ingredient_type_ID = ingredient_type.Ingredient_ID_type GROUP BY Dishes_id"
    foodALL_df = pd.read_sql(command2, con=conn)
    #加上食譜材料標籤
    food_df['genres']=food_df['genres']+","+ foodALL_df["Ingredient_title"]
    print(food_df)
    
    command3 ="SELECT user_dislike.member_id, ingredient.Ingredient_title FROM user_dislike LEFT JOIN ingredient ON user_dislike.Ingredient_ID=ingredient.Ingredient_ID WHERE member_id=\"{}\"".format(i)
    user_dislike_df = pd.read_sql(command3, con=conn)
    print(user_dislike_df)
    
    # z=int(input("請輸入您的冰箱id: "))
    # # Lawrence_recommendation=Lawrence_recommendation[['dishes_id']]
    command4 = "SELECT fridge_contain.FridgeID,fridge_link.member_ID,Ingredient.Ingredient_title FROM (`fridge_contain` LEFT JOIN ingredient ON fridge_contain.Ingredient_ID=ingredient.Ingredient_ID)RIGHT JOIN fridge_link ON fridge_link.FridgeID=fridge_contain.FridgeID WHERE fridge_link.member_id=\"{}\"".format(i)
    Fridge_ingredient = pd.read_sql(command4, con=conn)
    
    command5 = "SELECT user_dislike_type.member_id, ingredient_type.ingredient_type FROM user_dislike_type LEFT JOIN ingredient_type ON user_dislike_type.ingredient_type_ID=ingredient_type.Ingredient_ID_type WHERE member_id=\"{}\"".format(i)
    user_dislike_type_df = pd.read_sql(command5, con=conn)
    
    command6 = "SELECT * FROM `user_m_or_v`WHERE member_id=\"{}\"".format(i)
    user_M_or_V = pd.read_sql(command6, con=conn)
    
    #篩除掉不喜歡的大類
    
    print(user_dislike_type_df)
    user_no_rating=foodALL_df.copy()
    print(user_no_rating)
    user_no_rating_type=user_no_rating.ingredient_type.str.split(',',expand=True).shape[1]
    print(user_no_rating_type)
    b = []
    for y in range(0,user_no_rating_type):
             b.append("type"+str(y))
    print(b)    
    user_no_rating[b]= user_no_rating.ingredient_type.str.split(',',expand=True)
    
    if user_dislike_type_df.empty:
            pass
    else:
        for u in range(0,len(b)):
           user_no_rating=user_no_rating[~user_no_rating['type'+str(u)].isin(user_dislike_type_df['ingredient_type'])]
        user_no_rating.drop(['ingredient_type'], axis=1, inplace=True)
        user_no_rating.drop(['Ingredient_title'], axis=1, inplace=True)
        print(user_no_rating)  
    print(foodALL_df)
    user_dislike=foodALL_df.copy()
    user_dislike = user_dislike[user_dislike['Dishes_id'].isin(user_no_rating['Dishes_id'])]
    print(user_dislike)
    
    #篩除掉不喜歡的食物
    user_dislike_rating_shape=user_dislike.Ingredient_title.str.split(',',expand=True).shape[1]
    print(user_dislike_rating_shape)
    l1 = []
    # #     # # #
    # #     # # #
    for k in range(0,user_dislike_rating_shape):
          l1.append("title"+str(k))
    print(l1)
    # #     # # #
    
    user_dislike[l1]= user_dislike.Ingredient_title.str.split(',',expand=True)
    print(user_dislike)
    user_dislike.drop(['Ingredient_title'], axis=1, inplace=True)
    user_dislike.drop(['ingredient_type'], axis=1, inplace=True)
    # print(user_no_rating)
    # print(user_dislike_df)
    #     # # print("\n")
    # user_no_rating_recommemdation=user_no_rating
    if user_dislike_df.empty:
         pass
    else:
         for k in range(0,len(l1)):
             user_dislike=user_dislike[~user_dislike['title'+str(k)].isin(user_dislike_df['Ingredient_title'])]
         print(user_dislike)
    
    food_df = food_df[food_df['Dishes_id'].isin(user_dislike['Dishes_id'])]
    print(food_df)
    
    #篩除掉吃葷還是吃素
    
    missing_values = ['na', '--', '?', '-', 'None', 'none', 'non']
    
    a1=user_M_or_V[user_M_or_V["M_or_V"].isin(["葷食"])]
    print(a1)
    if a1.empty:
        food_df=food_df[food_df['M_or_V'].isin(user_M_or_V['M_or_V'])]
        
    else:
        pass
    print(food_df)
    
    food_df.drop(['M_or_V'], axis=1, inplace=True)
    
    
    food_df['genres'] = food_df.genres.str.split('.')
    
    food_df.isna().sum()
    
    food_df.Dishes_id = food_df.Dishes_id.astype('int32')
    
    food_with_genres = food_df.copy(deep=True)
    
    
    
    x = []
    for index, row in food_df.iterrows():
        x.append(index)
        for genres in row['genres']:
            out = "".join(genres.split())
            food_with_genres.at[index, out] = 1
    
    pd.set_option('display.max_columns',10)
    print(food_with_genres)
    
    food_with_genres = food_with_genres.fillna(0)
    print(food_with_genres)#.head(3)
    
    #如果沒有會員評分
    if user_rating.empty:
        user_fridge_contain=foodALL_df.copy()
        user_fridge_contain=user_fridge_contain[user_fridge_contain['Dishes_id'].isin(food_with_genres['Dishes_id'])]
        print(user_fridge_contain)
        
        copy = user_fridge_contain.copy(deep=True)
        print(copy)
        top20=user_fridge_contain.index[:20].tolist()
        
        user_fridge_contain = copy.loc[top20, :]
        
        user_fridge_contain=user_fridge_contain.reset_index()
        # 现在我们可以按喜好降序显示前20部食譜
        print('推荐的食譜列表：\n',user_fridge_contain)
        
        
        df = user_fridge_contain.set_index(["Dishes_id"])
        print(df)
        s = df["Ingredient_title"]
        print(s)
        df = s.str.split(",", expand=True)
        s=df.stack()
        s = s.reset_index(drop=True, level=-1)
              
        df = s.reset_index()
        df = df.rename(columns={0: "Ingredient_title"})
        print(df)
        df = df[~df['Ingredient_title'].isin(Fridge_ingredient['Ingredient_title'])]
        print(df)
        
        
        df=df.groupby('Dishes_id',as_index=False).agg(lambda x: x.tolist())
        print(df)
        df['Length'] = df.Ingredient_title.str.len()
        df.sort_values('Length')
        # # df = df[df.Length < 6]
        print(df)
        lst = list(df.columns)
        
        lst.insert(0,'member_id')
        
        food_recommemdation=df.reindex(columns=lst,fill_value=i)
    
        food_recommemdation['Ingredient_title'] = [','.join(i) if isinstance(i, list) else i for i in food_recommemdation['Ingredient_title']]
        
        food_recommemdation.rename(columns = {'Ingredient_title':'lacked_Ingredient_title'}, inplace = True)
        print(food_recommemdation)
    
        cursor.execute("DELETE FROM member_recommendation WHERE member_id=\"{}\"".format(i))
        conn.commit()
    
        
        food_recommemdation.to_sql('member_recommendation', con=co, if_exists='append',index=False)
    
    
    
    else:
        df_rating=user_rating['rating'].astype(float)    
        df_title=user_rating['title']
        food_dict = {
                        "rating": df_rating,
                        "title": df_title
        }
        Lawrence_food_ratings = pd.DataFrame(food_dict, columns= ['title', 'rating'])
        print(Lawrence_food_ratings)
        
        
        Lawrence_food_Id = food_df[food_df['title'].isin(Lawrence_food_ratings['title'])]
        
        print(Lawrence_food_Id)
        # 将Lawrence食譜Id和评级合并到lawrence_food_ratings数据框架中.
        # 此操作通过标题列隐式合并两个数据帧.
        Lawrence_food_ratings = pd.merge(Lawrence_food_Id, Lawrence_food_ratings)
        
        # 删除我们不需要的信息，比如类型
        Lawrence_food_ratings = Lawrence_food_ratings.drop(['genres'], 1)
        
        # 劳伦斯的最终文件
        print(Lawrence_food_ratings)
        # 通过输出两者都存在的影片来过滤选择，Lawrence_food_ratings和food_with_genre。
        Lawrence_genres_df = food_with_genres[food_with_genres.Dishes_id.isin(Lawrence_food_ratings.Dishes_id)]
        # Lawrence_genres_df
        
        # 首先，将index重置为default并删除现有索引。
        Lawrence_genres_df.reset_index(drop=True, inplace=True)
        
        # 接下来，去掉多余的列
        Lawrence_genres_df.drop(['Dishes_id', 'title', 'genres'], axis=1, inplace=True)
        
        # 我们来确认一下数据的形状，以便于做矩阵乘法。
        # print('Shape of Lawrence_food_ratings is:', Lawrence_food_ratings.shape)
        # print('Shape of Lawrence_genres_df is:', Lawrence_genres_df.shape)
        
        # 我们来求劳伦斯评级列的劳伦斯- genres_df转置的点积.  做乘积
        Lawrence_profile = Lawrence_genres_df.T.dot(Lawrence_food_ratings.rating)
        
        # 将索引设置为Dishes_id。
        food_with_genres = food_with_genres.set_index(food_with_genres.Dishes_id)
        
        # 删除四个不必要的列。
        food_with_genres.drop(['Dishes_id', 'title', 'genres'], axis=1, inplace=True)
        #print(food_with_genres)
        # 将类型数乘以权重，然后取加权平均值。   计算相似度，再去做归一化
        recommendation_table_df = (food_with_genres.dot(Lawrence_profile) / Lawrence_profile.sum())
        print(recommendation_table_df)
        # 将值从大到小排序
        recommendation_table_df.sort_values(ascending=False, inplace=True)
        
        
        # 首先，我们复制原始的food_df
        copy = food_df.copy(deep=True)
        
        # 然后将它的索引设置为Dishes_id
        copy = copy.set_index('Dishes_id', drop=True)
        
        # 接下来，我们列出我们在上面定义的前20个推荐的食譜id
        top_20_index = recommendation_table_df.index[:20].tolist()
        
        # 最后，我们将这些索引从复制的food_df中切片并保存到一个变量中
        recommended_food = copy.loc[top_20_index, :]
        recommended_food=recommended_food.reset_index()
        # 现在我们可以按喜好降序显示前20部食譜
        print('推荐的食譜列表：\n',recommended_food)
        
        
        #篩除掉冰箱剩餘食材----------------------------------------------------------------------------
        
        Lawrence_recommendation =foodALL_df[foodALL_df['Dishes_id'].isin(recommended_food['Dishes_id'])]
        # print(Lawrence_recommendation)
        Lawrence_recommendation = Lawrence_recommendation.set_index('Dishes_id')
        Lawrence_recommendation = Lawrence_recommendation.reindex(index=recommended_food['Dishes_id'])
        Lawrence_recommendation = Lawrence_recommendation.reset_index()
        Lawrence_recommendation.drop(['ingredient_type'], axis=1, inplace=True)
        print(Lawrence_recommendation)
        
        # Lawrence_recommendation.rename(columns = {'Dishes_id':'dishes_id'}, inplace = True)
        df = Lawrence_recommendation.set_index(["Dishes_id"])
    
        s = df["Ingredient_title"]
        print(s)
        df = s.str.split(",", expand=True)
        s=df.stack()
        s = s.reset_index(drop=True, level=-1)
          
        df = s.reset_index()
        df = df.rename(columns={0: "Ingredient_title"})
        print(df)
        
        print(Fridge_ingredient)
        df = df[~df['Ingredient_title'].isin(Fridge_ingredient['Ingredient_title'])]
        print(df)
    
    
        df=df.groupby('Dishes_id',as_index=False).agg(lambda x: x.tolist())
        print(df)
        
        df = df.set_index('Dishes_id')
        df = df.reindex(index=Lawrence_recommendation['Dishes_id'])
        df = df.reset_index()
        df['Length'] = df.Ingredient_title.str.len()
        # # ## df.sort_values('Length')
        # # # # df = df[df.Length < 6]
        print(df)
        lst = list(df.columns)
        
        lst.insert(0,'member_id')
        
        food_recommemdation=df.reindex(columns=lst,fill_value=i)
    
        food_recommemdation['Ingredient_title'] = [','.join(i) if isinstance(i, list) else i for i in food_recommemdation['Ingredient_title']]
        
        food_recommemdation.rename(columns = {'Ingredient_title':'lacked_Ingredient_title'}, inplace = True)
        print(food_recommemdation)
        cursor.execute("DELETE FROM member_recommendation WHERE member_id=\"{}\"".format(i))
        conn.commit()
    
      
        food_recommemdation.to_sql('member_recommendation', con=co, if_exists='append',index=False)    