import os

from flask import Flask
from extensions import db, bcrypt, jwt
from blueprints import post, user
from database import BlacklistedToken

app = Flask(__name__)

db_uri = "sqlite:///./database.db"
if os.environ.get("NAMESPACE") == "heroku" and "DATABASE_URL" in os.environ:
    db_uri = os.environ["DATABASE_URL"]

secret_key = "itsasecrettoeverybody"
if os.environ.get('NAMESPACE') == 'heroku' and "SECRET_KEY" in os.environ:
    secret_key = os.environ["SECRET_KEY"]

app.config["SQLALCHEMY_DATABASE_URI"] = db_uri
app.config["JWT_SECRET_KEY"] = secret_key
app.config["JWT_BLACKLIST_ENABLED"] = True
app.config["JWT_BLACKLIST_TOKEN_CHECKS"] = "access"

db.init_app(app)
jwt.init_app(app)
bcrypt.init_app(app)

app.register_blueprint(user.bp)
app.register_blueprint(post.bp)


@app.before_first_request
def init_database():
    db.create_all()


@jwt.token_in_blacklist_loader
def check_blacklist(decoded_loader):
    jti = decoded_loader["jti"]
    if BlacklistedToken.query.get(jti) is not None:
        return True
    return False


if __name__ == "__main__":
    app.run()
