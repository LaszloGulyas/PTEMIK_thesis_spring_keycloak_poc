import { Component, OnInit } from '@angular/core';
import {GlobalService} from "../../services/global.service";

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  errorMessage: string | null = null;

  constructor(private globalService: GlobalService) { }

  ngOnInit(): void {
    this.errorMessage = this.globalService.getErrorMessage();
    this.globalService.setErrorMessage(null);
  }

}
