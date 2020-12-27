//
//  ViewController.swift
//  currency
//
//  Created by marcelo on 26/12/2020.
//  Copyright © 2020 marcelo. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    let baseUrl: String = "https://api.exchangerate.host/latest?base=USD&symbols=BTC,EUR,BRL,GBP"
    var rates: Dictionary<String, Double>!
    
    @IBOutlet var updateTimeLabel: UITextView!
    @IBOutlet var usdTExtField: UITextField!
    @IBOutlet var eurTExtField: UITextField!
    @IBOutlet var gbpTExtField: UITextField!
    @IBOutlet var brlTExtField: UITextField!
    @IBOutlet var btcTExtField: UITextField!

    @IBAction func update()->Void {
        guard let u = URL(string: self.baseUrl) else {
            return
        }
        URLSession.shared.dataTask(with: u) {
            (data, response, error) in
            guard let data = data else {
                return
                
            }
            do{
                let currencyData:Response = try JSONDecoder().decode(Response.self, from: data)
                self.rates = currencyData.rates
                print(self.rates)
                let formatter = DateFormatter()
                formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                DispatchQueue.main.async {
                    self.updateTimeLabel.text = formatter.string(from: Date())
                }
                
            }catch let error {
                print("\(error)")
            }
            
            }.resume()

    }

    override func viewDidLoad() {
        super.viewDidLoad()
        update()
        self.usdTExtField.keyboardType = .numberPad
        self.eurTExtField.keyboardType = .numberPad
        self.gbpTExtField.keyboardType = .numberPad
        self.brlTExtField.keyboardType = .numberPad
        self.btcTExtField.keyboardType = .numberPad
        self.usdTExtField.addTarget(self, action: #selector(ViewController.getRatesFromUSD(_:)), for: .editingChanged)
        self.eurTExtField.addTarget(self, action: #selector(ViewController.getRatesFromEUR(_:)), for: .editingChanged)
        self.gbpTExtField.addTarget(self, action: #selector(ViewController.getRatesFromGBP(_:)), for: .editingChanged)
        self.brlTExtField.addTarget(self, action: #selector(ViewController.getRatesFromBRL(_:)), for: .editingChanged)
        self.btcTExtField.addTarget(self, action: #selector(ViewController.getRatesFromBTC(_:)), for: .editingChanged)
    }
    

    @objc func getRatesFromUSD(_ textField: UITextField) {
        let value = usdTExtField.text
        guard let usd = Double(value!) else
        {
            return
        }
        let eur = usd * Double(rates["EUR"]!)
        let gbp = usd * Double(rates["GBP"]!)
        let brl = usd * Double(rates["BRL"]!)
        let btc = usd * Double(rates["BTC"]!)
        eurTExtField.text = String(eur)
        gbpTExtField.text = String(gbp)
        brlTExtField.text = String(brl)
        btcTExtField.text = String(format: "%.15f", btc)
    }
    @objc func getRatesFromEUR(_ textField: UITextField) {
        let value = eurTExtField.text
        guard let eur = Double(value!) else
        {
            return
        }
        let usd = eur / Double(rates["EUR"]!)
        let gbp = usd * Double(rates["GBP"]!)
        let brl = usd * Double(rates["BRL"]!)
        let btc = usd * Double(rates["BTC"]!)
        usdTExtField.text = String(usd)
        gbpTExtField.text = String(gbp)
        brlTExtField.text = String(brl)
        btcTExtField.text = String(format: "%.15f", btc)
    }
    @objc func getRatesFromGBP(_ textField: UITextField) {
        let value = gbpTExtField.text
        guard let gbp = Double(value!) else
        {
            return
        }
        let usd = gbp / Double(rates["GBP"]!)
        let eur = usd * Double(rates["EUR"]!)
        let brl = usd * Double(rates["BRL"]!)
        let btc = usd * Double(rates["BTC"]!)
        eurTExtField.text = String(eur)
        usdTExtField.text = String(usd)
        brlTExtField.text = String(brl)
        btcTExtField.text = String(format: "%.15f", btc)
    }
    @objc func getRatesFromBRL(_ textField: UITextField) {
        let value = brlTExtField.text
        guard let brl = Double(value!) else
        {
            return
        }
        let usd = brl / Double(rates["BRL"]!)
        let eur = usd * Double(rates["EUR"]!)
        let gbp = usd * Double(rates["GBP"]!)
        let btc = usd * Double(rates["BTC"]!)
        eurTExtField.text = String(eur)
        gbpTExtField.text = String(gbp)
        usdTExtField.text = String(usd)
        btcTExtField.text = String(format: "%.15f", btc)
    }
    @objc func getRatesFromBTC(_ textField: UITextField) {
        let value = btcTExtField.text
        guard let btc = Double(value!) else
        {
            return
        }
        let usd = btc / Double(rates["BTC"]!)
        let eur = usd * Double(rates["EUR"]!)
        let gbp = usd * Double(rates["GBP"]!)
        let brl = usd * Double(rates["BRL"]!)
        eurTExtField.text = String(eur)
        gbpTExtField.text = String(gbp)
        brlTExtField.text = String(brl)
        usdTExtField.text = String(format: "%.15f", usd)
    }
}

