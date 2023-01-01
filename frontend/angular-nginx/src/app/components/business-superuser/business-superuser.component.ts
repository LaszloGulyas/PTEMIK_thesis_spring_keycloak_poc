import { Component, OnInit } from '@angular/core';
import {BusinessService} from "../../services/business.service";
import {Router} from "@angular/router";
import {GlobalService} from "../../services/global.service";

@Component({
  selector: 'app-business-superuser',
  templateUrl: './business-superuser.component.html',
  styleUrls: ['./business-superuser.component.css']
})
export class BusinessSuperuserComponent implements OnInit {

  isContentLoaded: boolean = false;
  content : string | null = "";

  constructor(private businessService: BusinessService, private router: Router, private globalService: GlobalService) {
  }

  ngOnInit(): void {
    this.businessService.fetchSuperuserContent().subscribe(
      response => {
        this.content = response.content;
        this.isContentLoaded = true;
        console.log("Fetching superuser content completed!");
      },
      errResponse => {
        this.globalService.setErrorMessage(errResponse.error.errorMessage);
        console.log("Fetching superuser content failed!");
        this.router.navigate(['error']);
      });
  }

}
