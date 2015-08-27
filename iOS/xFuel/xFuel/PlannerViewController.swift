//
//  FirstViewController.swift
//  xFuel
//
//  Created by Edvard Holst on 17/08/15.
//  Copyright (c) 2015 Zygote Labs. All rights reserved.
//

import UIKit

class PlannerViewController: UIViewController, UITextFieldDelegate {
    

    @IBOutlet weak var textFieldDeparture: UITextField!
    @IBOutlet weak var textFieldArrival: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.textFieldDeparture.delegate = self;
        self.textFieldArrival.delegate = self;
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        self.view.endEditing(true)
        return false
    }

}

