from flask_cors import CORS
from flask import *

import sqlite3
db = sqlite3.connect("database.db", check_same_thread=False)

app = Flask(__name__)

@app.route("/post/activity", methods=["POST"])
def PostActivity():
    print(request.get_json())
    return 200



app.run("0.0.0.0", 9200)