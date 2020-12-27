//
//  Response.swift
//  currency
//
//  Created by marcelo on 26/12/2020.
//  Copyright © 2020 marcelo. All rights reserved.
//

import Foundation

struct Response: Codable {
    let rates: Dictionary<String, Double>
}
