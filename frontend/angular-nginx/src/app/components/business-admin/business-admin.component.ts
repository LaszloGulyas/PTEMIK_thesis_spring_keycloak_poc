import { Component, OnInit } from '@angular/core';
import {BusinessService} from "../../services/business.service";
import {Router} from "@angular/router";
import {GlobalService} from "../../services/global.service";

@Component({
  selector: 'app-business-admin',
  templateUrl: './business-admin.component.html',
  styleUrls: ['./business-admin.component.css']
})
export class BusinessAdminComponent implements OnInit {

  isContentLoaded: boolean = false;
  content : string | null = "";

  constructor(private businessService: BusinessService, private router: Router, private globalService: GlobalService) {
  }

  ngOnInit(): void {
    this.businessService.fetchAdminContent().subscribe(
      response => {
        this.content = response.content;
        this.isContentLoaded = true;
        console.log("Fetching admin content completed!");
      },
      errResponse => {
        this.globalService.setErrorMessage(errResponse.error.errorMessage);
        console.log("Fetching admin content failed!");
        this.router.navigate(['error']);
      });
  }

}
