import { Component, OnInit } from '@angular/core';
import {BusinessService} from "../../services/business.service";
import {Router} from "@angular/router";
import {GlobalService} from "../../services/global.service";

@Component({
  selector: 'app-business-user',
  templateUrl: './business-user.component.html',
  styleUrls: ['./business-user.component.css']
})
export class BusinessUserComponent implements OnInit {

  isContentLoaded: boolean = false;
  content: string | null = "";

  constructor(private businessService: BusinessService, private router: Router, private globalService: GlobalService) {
  }

  ngOnInit(): void {
    this.businessService.fetchUserContent().subscribe(
      response => {
        this.content = response.content;
        this.isContentLoaded = true;
        console.log("Fetching user content completed!");
      },
      errResponse => {
        this.globalService.setErrorMessage(errResponse.error.errorMessage);
        console.log("Fetching user content failed!");
        this.router.navigate(['error']);
      });
  }

}
