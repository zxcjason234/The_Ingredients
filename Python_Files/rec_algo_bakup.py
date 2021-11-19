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
    
#    i= str(input("請輸入會員id: "))
    command1 = "SELECT member_id,dishes.title,rating FROM `user_ratings` LEFT JOIN dishes ON user_ratings.Dishes_ID=dishes.Dishes_id WHERE member_id=\"{}\" ".format(i)
    user_rating = pd.read_sql(command1, con=conn)
    
    command2="SELECT dishes_ingredient.dishes_id,GROUP_CONCAT(ingredient_type.Ingredient_type)as ingredient_type,group_concat(Ingredient_title) as Ingredient_title FROM ((dishes_ingredient LEFT JOIN dishes ON dishes.Dishes_id=dishes_ingredient.Dishes_id)LEFT JOIN ingredient on dishes_ingredient.Ingredient_ID=ingredient.Ingredient_ID)INNER JOIN ingredient_type ON ingredient.ingredient_type_ID = ingredient_type.Ingredient_ID_type GROUP BY Dishes_id"
    foodALL_df = pd.read_sql(command2, con=conn)
    
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
    
    
    # 定义额外的 NaN标识符.
    missing_values = ['na', '--', '?', '-', 'None', 'none', 'non']
    
    # user_M_or_V[user_M_or_V.M_or_V=="葷食"]
    
    a1=user_M_or_V[user_M_or_V["M_or_V"].isin(["葷食"])]
    print("a1"+a1)
    if a1.empty:
        food_df=food_df[food_df['M_or_V'].isin(user_M_or_V['M_or_V'])]
    
    else:
        pass
    
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
    
    
    
    
    if user_rating.empty:
        user_no_rating=foodALL_df.copy()
        user_no_rating_type=user_no_rating.ingredient_type.str.split(',',expand=True).shape[1]
        b = []
        for y in range(0,user_no_rating_type):
             b.append("type"+str(y))
        print(b)    
        user_no_rating[b]= user_no_rating.ingredient_type.str.split(',',expand=True)
    
        print(user_no_rating)
        print(user_dislike_type_df)
        if user_dislike_type_df.empty:
            pass
        else:
            for u in range(0,len(b)):
                  user_no_rating=user_no_rating[~user_no_rating['type'+str(u)].isin(user_dislike_type_df['ingredient_type'])]
        print(user_no_rating)  
        
        
        
        user_no_rating_shape=user_no_rating.Ingredient_title.str.split(',',expand=True).shape[1]
        print(user_no_rating_shape)
        l1 = []
        # # #
        # # #
        for k in range(0,user_no_rating_shape):
              l1.append("title"+str(k))
        print(l1)
        # # #
        user_no_rating[l1]= user_no_rating.Ingredient_title.str.split(',',expand=True)
        print(user_no_rating)
        user_no_rating.drop(['Ingredient_title'], axis=1, inplace=True)
        user_no_rating.drop(['ingredient_type'], axis=1, inplace=True)
        print(user_no_rating)
        print(user_dislike_df)
        # # print("\n")
        user_no_rating_recommemdation=user_no_rating
        if user_dislike_df.empty:
              pass
        else:
              for k in range(0,len(l1)):
                  user_no_rating_recommemdation=user_no_rating_recommemdation[~user_no_rating_recommemdation['title'+str(k)].isin(user_dislike_df['Ingredient_title'])]
        # #user_no_rating_recommemdation=user_no_rating_recommemdation[['dishes_id']]
        
        print(foodALL_df)
        user_no_rating_recommemdation=foodALL_df[foodALL_df['dishes_id'].isin(user_no_rating_recommemdation['dishes_id'])]
        print(user_no_rating_recommemdation)
        df = user_no_rating_recommemdation.set_index(["dishes_id"])
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
    
    
        df=df.groupby('dishes_id',as_index=False).agg(lambda x: x.tolist())
        print(df)
        df['Length'] = df.Ingredient_title.str.len()
        df.sort_values('Length')
        # # df = df[df.Length < 6]
        print(df)
        lst = list(df.columns)
        
        lst.insert(0,'member_id')
        
        food_recommemdation=df.reindex(columns=lst,fill_value=i)
    
        food_recommemdation['Ingredient_title'] = [','.join(i) if isinstance(i, list) else i for i in food_recommemdation['Ingredient_title']]
        print(food_recommemdation)
        food_recommemdation.rename(columns = {'Ingredient_title':'lacked_Ingredient_title'}, inplace = True)
        print(food_recommemdation)
        # # food_recommemdation.loc[:, 'Ingredient_title'] = food_recommemdation['Ingredient_title'].astype(str)
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
        #print(Lawrence_food_ratings.head())
        
        # 从food_df中提取食譜id，并使用食譜id更新lawrence_food_ratings。
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
    
        
        # foodALL_df['title'] = foodALL_df.title.str.split(',')
        #print(foodALL_df)
    
        print(foodALL_df)
    
        Lawrence_recommendation =foodALL_df[foodALL_df['dishes_id'].isin(recommended_food['Dishes_id'])]
        print(Lawrence_recommendation)
        Lawrence_recommendation = Lawrence_recommendation.set_index('dishes_id')
        Lawrence_recommendation = Lawrence_recommendation.reindex(index=recommended_food['Dishes_id'])
        Lawrence_recommendation = Lawrence_recommendation.reset_index()
        print(Lawrence_recommendation)
        Lawrence_recommendation=Lawrence_recommendation.rename(columns = {'Dishes_id':'dishes_id'})
        #food_ingredient= Lawrence_recommendation.title.str.split(',',expand=True)
        #print(food_ingredient.shape)
        l_type=Lawrence_recommendation.ingredient_type.str.split(',',expand=True).shape[1]
        b = []
        for y in range(0,l_type):
             b.append("type"+str(y))
        print(b)    
        Lawrence_recommendation[b]= Lawrence_recommendation.ingredient_type.str.split(',',expand=True)
    
        print(Lawrence_recommendation)
        print(user_dislike_type_df)
        if user_dislike_type_df.empty:
            pass
        else:
            for u in range(0,len(b)):
                  Lawrence_recommendation=Lawrence_recommendation[~Lawrence_recommendation['type'+str(u)].isin(user_dislike_type_df['ingredient_type'])]
        print(Lawrence_recommendation)    
        # # #print("\n")
        l=Lawrence_recommendation.Ingredient_title.str.split(',',expand=True).shape[1]
        l1 = []
        # # # #
        # # # #
        for j in range(0,l):
               l1.append("title"+str(j))
        print(l1)
        # # # #
        Lawrence_recommendation[l1]= Lawrence_recommendation.Ingredient_title.str.split(',',expand=True)
        Lawrence_recommendation.drop(['Ingredient_title'], axis=1, inplace=True)
        Lawrence_recommendation.drop(['ingredient_type'], axis=1, inplace=True)
        print(Lawrence_recommendation)
        
        # # # print("\n")
        if user_dislike_df.empty:
              pass   
        else:    
               for k in range(0,len(l1)):
                   Lawrence_recommendation=Lawrence_recommendation[~Lawrence_recommendation['title'+str(k)].isin(user_dislike_df['Ingredient_title'])]
    
        print(Lawrence_recommendation)
    
        Ingredient_recommendation=foodALL_df[foodALL_df['dishes_id'].isin(Lawrence_recommendation['dishes_id'])]
        Ingredient_recommendation = Ingredient_recommendation.set_index('dishes_id')
        Ingredient_recommendation = Ingredient_recommendation.reindex(index=Lawrence_recommendation['dishes_id'])
        Ingredient_recommendation = Ingredient_recommendation.reset_index()
        print(Ingredient_recommendation)
        
        df = Ingredient_recommendation.set_index(["dishes_id"])
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
    
    
        df=df.groupby('dishes_id',as_index=False).agg(lambda x: x.tolist())
        print(df)
        
        df = df.set_index('dishes_id')
        df = df.reindex(index=Lawrence_recommendation['dishes_id'])
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
        # # # # # food_recommemdation.loc[:, 'Ingredient_title'] = food_recommemdation['Ingredient_title'].astype(str)
        cursor.execute("DELETE FROM member_recommendation WHERE member_id=\"{}\"".format(i))
        conn.commit()
    
      
        food_recommemdation.to_sql('member_recommendation', con=co, if_exists='append',index=False)