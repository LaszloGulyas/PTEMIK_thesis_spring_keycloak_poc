import { Component, OnInit } from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";
import {GlobalService} from "../../services/global.service";

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrls: ['./delete-user.component.css']
})
export class DeleteUserComponent implements OnInit {

  isAccountDeleted : boolean = false;

  constructor(private userService: UserService, private router: Router, private globalService: GlobalService) {
  }
  ngOnInit(): void {
  }

  delete = () => {
    console.log("Delete account request started...");

    this.userService.deleteUser().subscribe(
      response => {
        this.globalService.setAuthToken(null);
        this.isAccountDeleted = true;
        console.log("Account deleted successfully!");
      },
      errResponse => {
        this.globalService.setErrorMessage(errResponse.error.errorMessage);
        console.log("Account deletion failed!");
        this.router.navigate(['error']);
      });
  }

}
