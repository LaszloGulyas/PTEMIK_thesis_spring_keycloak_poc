import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { BusinessUserComponent } from './components/business-user/business-user.component';
import { BusinessSuperuserComponent } from './components/business-superuser/business-superuser.component';
import { BusinessAdminComponent } from './components/business-admin/business-admin.component';
import { ErrorComponent } from './components/error/error.component';
import { UpdatePasswordComponent } from './components/update-password/update-password.component';
import { DeleteUserComponent } from './components/delete-user/delete-user.component';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    RegisterComponent,
    LoginComponent,
    BusinessUserComponent,
    BusinessSuperuserComponent,
    BusinessAdminComponent,
    ErrorComponent,
    UpdatePasswordComponent,
    DeleteUserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
