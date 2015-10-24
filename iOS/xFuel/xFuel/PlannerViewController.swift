//
//  FirstViewController.swift
//  xFuel
//
//  Created by Edvard Holst on 17/08/15.
//  Copyright (c) 2015 Zygote Labs. All rights reserved.
//

import UIKit
import CoreData

class PlannerViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var textFieldDeparture: UITextField!
    @IBOutlet weak var textFieldArrival: UITextField!
    @IBOutlet weak var aircraftPicker: UIPickerView!
    
    var people = [NSManagedObject]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.textFieldDeparture.delegate = self;
        self.textFieldArrival.delegate = self;
        
    }
    
    func saveAircraft(name: String, code: String) {
        
        let appDelegate =
        UIApplication.sharedApplication().delegate as! AppDelegate
        
        let managedContext = appDelegate.managedObjectContext!
        
        //2
        let entity =  NSEntityDescription.entityForName("Aircraft",
            inManagedObjectContext:
            managedContext)
        
        let aircraft = NSManagedObject(entity: entity!,
            insertIntoManagedObjectContext:managedContext)
        
        aircraft.setValue(name, forKey: "name")
        
    }
    
    override func viewDidAppear(animated: Bool) {
        // 1
        var nav = self.navigationController?.navigationBar
        // 2
        nav?.barStyle = UIBarStyle.Black
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

