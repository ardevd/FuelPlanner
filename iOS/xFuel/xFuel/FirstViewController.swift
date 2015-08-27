//
//  FirstViewController.swift
//  xFuel
//
//  Created by Edvard Holst on 17/08/15.
//  Copyright (c) 2015 Zygote Labs. All rights reserved.
//

import UIKit

class FirstViewController: UIViewController {
    
    @IBOutlet weak var originText: UITextField!
    @IBOutlet weak var destinationText: UITextField!
    
    var aircrafts = ["A320","A330","A340","A350"]

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
    }
   

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1
    }
    
    /* func pickerView(pickerView: UIPickerView, numberOfRowsInComponent: component) {
    return colors.count
    }*/
    
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return aircrafts.count
    }
    
    // pragma MARK: UIPickerViewDelegate
    
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String! {
        return aircrafts[row]
    }


}

