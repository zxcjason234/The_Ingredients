#-*-coding:utf-8-*-
from flask import Flask
from flask import request
import os
import socket
import rec_algo as rec
import rec_algo_v2 as rec_v2



hostname = socket.gethostname()
local_ip = socket.gethostbyname(hostname)


app = Flask(__name__)
# basedir=os.path.abspath(os.path.dirname(__file__))
app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN']=True
app.config['SQLALCHEMY_TRACK_MODIFICATIONS']=True

@app.route('/')
def test():
    return '服务器正常运行'



#此方法处理用户注册
@app.route('/rec',methods=['POST'])
def register():
    
    #回傳使用者id
    username=request.form['username']
#    password=request.form['password']

    print('username:'+username)
#    print('password:'+password)
#    return '注册成功'
    
    #為使用者推薦演算法
    rec_v2.rec(username)
    
    return username
    
    

if __name__ == '__main__':
    app.run(host=local_ip)

