import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {LoginFormDataModel} from "../models/loginFormData.model";
import {LoginResponseModel} from "../models/loginResponse.model";
import {RegisterFormDataModel} from "../models/registerFormData.model";
import {GlobalService} from "./global.service";
import {UpdatePasswordFormDataModel} from "../models/updatePasswordFormData.model";

const SERVER_URL = environment.baseUrl;
const BASE_URL = SERVER_URL + '/api/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) {
  }

  registerUser(registerFormData: RegisterFormDataModel): Observable<any> {
    return this.httpClient.post<any>(BASE_URL + "/register", registerFormData);
  }

  loginUser(userFormData: LoginFormDataModel): Observable<LoginResponseModel> {
    return this.httpClient.post<LoginResponseModel>(BASE_URL + "/login", userFormData);
  }

  deleteUser(): Observable<any> {
    return this.httpClient.delete(BASE_URL, {headers: this.createHeaderWithBearerToken()});
  }

  updatePassword(updatePasswordFormData: UpdatePasswordFormDataModel): Observable<any> {
    return this.httpClient.put(BASE_URL + "/update-password", updatePasswordFormData, {headers: this.createHeaderWithBearerToken()});
  }

  private createHeaderWithBearerToken() {
    const token = this.globalService.getAuthToken();
    return new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
  }

}
