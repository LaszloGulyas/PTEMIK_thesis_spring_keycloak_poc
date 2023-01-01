import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {LoginFormDataModel} from "../models/loginFormData.model";
import {LoginResponseModel} from "../models/loginResponse.model";

const SERVER_URL = environment.baseUrl;
const BASE_URL = SERVER_URL + '/api/user/';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) {
  }

  loginUser(userFormData: LoginFormDataModel): Observable<LoginResponseModel> {
    return this.httpClient.post<LoginResponseModel>(BASE_URL + "login", userFormData);
  }

}
