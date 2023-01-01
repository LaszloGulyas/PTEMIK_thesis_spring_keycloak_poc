import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {UpdatePasswordComponent} from "./components/update-password/update-password.component";
import {DeleteUserComponent} from "./components/delete-user/delete-user.component";
import {BusinessUserComponent} from "./components/business-user/business-user.component";
import {BusinessSuperuserComponent} from "./components/business-superuser/business-superuser.component";
import {BusinessAdminComponent} from "./components/business-admin/business-admin.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'update-password', component: UpdatePasswordComponent},
  {path: 'delete-account', component: DeleteUserComponent},
  {path: 'user-page', component: BusinessUserComponent},
  {path: 'superuser-page', component: BusinessSuperuserComponent},
  {path: 'admin-page', component: BusinessAdminComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
