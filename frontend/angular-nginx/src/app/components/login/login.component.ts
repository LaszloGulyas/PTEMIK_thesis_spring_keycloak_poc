import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {FormBuilder} from "@angular/forms";
import {Router} from "@angular/router";
import {GlobalService} from "../../services/global.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  isAuthenticated : boolean = false;

  userForm = this.formBuilder.group({
    'username': [''],
    'password': [''],
  });

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router,
              private globalService: GlobalService) {
  }

  ngOnInit(): void {
  }

  submit = () => {
    console.log("Login form submission started...");

    const loginRequest = {
      username: this.userForm.value.username,
      password: this.userForm.value.password
    }

    this.userService.loginUser(loginRequest).subscribe(
      response => {
        this.isAuthenticated = true;
        this.globalService.setAuthToken(response.token);
        console.log("Login form submission completed!");
      },
      errResponse => {
        this.globalService.setErrorMessage(errResponse.error.errorMessage);
        console.log("Login form submission failed!");
        this.router.navigate(['error']);
      });
  }

}
