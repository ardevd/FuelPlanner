//
//  HistoryItem.swift
//  xFuel
//
//  Created by Edvard Holst on 28/08/15.
//  Copyright (c) 2015 Zygote Labs. All rights reserved.
//

import Foundation
import CoreData

class HistoryItem: NSManagedObject {

    @NSManaged var aircraft: String
    @NSManaged var departure: String
    @NSManaged var arrival: String
    @NSManaged var wantMetric: NSNumber

}
