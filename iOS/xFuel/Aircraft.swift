//
//  Aircraft.swift
//  xFuel
//
//  Created by Edvard Holst on 28/08/15.
//  Copyright (c) 2015 Zygote Labs. All rights reserved.
//

import Foundation
import CoreData

class Aircraft: NSManagedObject {

    @NSManaged var name: String
    @NSManaged var code: String

}
