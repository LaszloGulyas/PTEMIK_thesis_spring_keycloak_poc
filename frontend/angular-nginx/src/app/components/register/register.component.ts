import { Component, OnInit } from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";
import {GlobalService} from "../../services/global.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  isRegisteredSuccessfully : boolean = false;

  userForm = this.formBuilder.group({
    'username': [''],
    'password': [''],
    'email': ['']
  });

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router,
              private globalService: GlobalService) {
  }

  ngOnInit(): void {
  }

  submit = () => {
    console.log("Register form submission started...");

    const registerRequest = {
      username: this.userForm.value.username,
      password: this.userForm.value.password,
      email:  this.userForm.value.email
    }

    this.userService.registerUser(registerRequest).subscribe(
      response => {
        this.isRegisteredSuccessfully = true;
        console.log("Register form submission completed!");
      },
      errResponse => {
        this.globalService.setErrorMessage(errResponse.error.errorMessage);
        console.log("Register form submission failed!");
        this.router.navigate(['error']);
      });
  }
}
