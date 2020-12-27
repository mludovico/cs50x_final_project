(function (doc, win){
  let reloadRequest = new XMLHttpRequest();
  reloadRequest.onreadystatechange = function(){
    if(reloadRequest.readyState == 4 && reloadRequest.status == 200) {
      let data = reloadRequest.response;
      updatedField.innerHTML = JSON.parse(data)['updated'];
    }
  }
  
  let exchangeRequest = new XMLHttpRequest();
  exchangeRequest.onreadystatechange = function(){
    if(exchangeRequest.readyState == 4 && exchangeRequest.status == 200) {
      let data = exchangeRequest.response;
      usdField.value = JSON.parse(data)['USD'];
      eurField.value = JSON.parse(data)['EUR'];
      gbpField.value = JSON.parse(data)['GBP'];
      brlField.value = JSON.parse(data)['BRL'];
      btcField.value = JSON.parse(data)['BTC'];
    }
  }
  
  let form = document.getElementById('form');
  let usdField = document.getElementById('usd');
  let eurField = document.getElementById('eur');
  let gbpField = document.getElementById('gbp');
  let brlField = document.getElementById('brl');
  let btcField = document.getElementById('btc');
  let updatedField = document.getElementById('updated-text');
  let updateButton = document.getElementById('update-btn');
  
  updateButton.addEventListener(
    'click',
    function (e) {
      reloadRequest.open('GET', '/reload');
      reloadRequest.send();
    }
  );

  form.onsubmit = function (e) {
    e.preventDefault();
    e.stopPropagation();
  }

  function requestAjax(value, source) {
    exchangeRequest.open('POST', '/getExchangeRate');
    data = new FormData();
    data.append('value', value);
    data.append('source', source);
    exchangeRequest.send(data);
  }

  usdField.oninput = function (e){
    requestAjax(e.target.value, 'USD');
    e.target.focus()
  }

  eurField.oninput = function (e){
    requestAjax(e.target.value, 'EUR');
  }

  gbpField.oninput = function (e){
    requestAjax(e.target.value, 'GBP');
  }

  brlField.oninput = function (e){
    requestAjax(e.target.value, 'BRL');
  }

  btcField.oninput = function (e){
    requestAjax(e.target.value, 'BTC');
  }

})(document, window);