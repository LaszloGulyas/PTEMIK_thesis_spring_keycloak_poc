import { Component, OnInit } from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";
import {GlobalService} from "../../services/global.service";

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent implements OnInit {

  isPasswordUpdated : boolean = false;

  userForm = this.formBuilder.group({
    'password': ['']
  });

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router,
              private globalService: GlobalService) {
  }
  ngOnInit(): void {
  }

  submit = () => {
    console.log("Update-password form submission started...");

    const updatePasswordRequest = {
      password: this.userForm.value.password
    }

    this.userService.updatePassword(updatePasswordRequest).subscribe(
      () => {
        this.isPasswordUpdated = true;
        console.log("Update-password form submission completed!");
      },
      errResponse => {
        this.globalService.setErrorMessage(errResponse.error.errorMessage);
        console.log("Update-password form submission failed!");
        this.router.navigate(['error']);
      });
  }

}
