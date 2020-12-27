import os
from datetime import datetime
from time import time
from flask import Flask, jsonify, redirect, render_template, request
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
from requests import get

# Configure application
app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True

# Ensure responses aren't cached
@app.after_request
def after_request(response):
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response

def usd(value):
    """Format value as USD."""
    return f"${value:,.2f}"

def dtFormat(value):
    """Format milliseconds to DateTime"""
    return datetime.fromtimestamp(value/1000).strftime("%Y-%m-%d %H:%M:%S")
  
# Custom filter
app.jinja_env.filters["usd"] = usd
app.jinja_env.filters["dtFormat"] = dtFormat

rates = {
  "USD": 0,
  "EUR": 0,
  "GBP": 0,
  "BRL": 0,
  "BTC": 0
}

updated = ""

@app.route("/")
def index():
  update()
  return render_template("index.html", updated=updated)

def calculate(value, source):
  data = {}
  data["USD"] = float(value) / rates[source]
  for key in rates.keys():
    data[key] = data["USD"] * float(rates[key])
  return data

@app.route("/getExchangeRate", methods=["GET", "POST"])
def fromUsd():
  errors = []
  if request.method != "POST":
    errors.append("Method not allowed")
  if not request.form.get("value"):
    errors.append("Missing value parameter")
  try:
    float(request.form.get("value"))
  except:
    errors.append("Invalid value parameter")
  if not request.form.get("source"):
    errors.append("Missing source parameter")
  if request.form.get("source") not in rates.keys():
    errors.append("Invalid source parameter")
  if len(errors) > 0:
    return jsonify({"Errors": errors})
  value = request.form.get("value")
  source = request.form.get("source")
  data = calculate(value, source)
  data["updated"] = updated
  return jsonify(data)

def update():
  baseUrl = "https://api.exchangerate.host/latest"
  params = {"base":"usd", "symbols":"USD,EUR,GBP,BRL,BTC"}
  response = get(baseUrl, params=params)
  if response.status_code != 200:
    print("Request error. Could not get data from api")
    return
  global rates
  rates = response.json()["rates"]
  print(rates)
  global updated
  updated = dtFormat(round(time() * 1000))

@app.route("/reload")
def reload():
  update()
  return jsonify({"updated": updated})