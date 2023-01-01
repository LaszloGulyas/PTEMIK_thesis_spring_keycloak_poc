import {Component, OnInit} from '@angular/core';
import {GlobalService} from "../../services/global.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(public globalService: GlobalService, private router: Router) {
  }

  ngOnInit(): void {
  }

  logout() {
    this.globalService.logout();
    this.router.navigate(['home']);
  }
}
