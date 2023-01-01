import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {BusinessResponseModel} from "../models/businessResponse.model";
import {environment} from "../../environments/environment";
import {GlobalService} from "./global.service";

const SERVER_URL = environment.baseUrl;
const BASE_URL = SERVER_URL + '/api/business/';

@Injectable({
  providedIn: 'root'
})
export class BusinessService {

  constructor(private httpClient: HttpClient, private globalService: GlobalService) {
  }

  fetchUserContent(): Observable<BusinessResponseModel> {
    return this.httpClient.get<BusinessResponseModel>(
      BASE_URL + "user/execute", {headers: this.createHeaderWithBearerToken()});
  }

  fetchSuperuserContent(): Observable<BusinessResponseModel> {
    return this.httpClient.get<BusinessResponseModel>(
      BASE_URL + "super-user/execute", {headers: this.createHeaderWithBearerToken()});
  }

  fetchAdminContent(): Observable<BusinessResponseModel> {
    return this.httpClient.get<BusinessResponseModel>(
      BASE_URL + "admin/execute", {headers: this.createHeaderWithBearerToken()});
  }

  private createHeaderWithBearerToken() {
    const token = this.globalService.getAuthToken();
    return new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
  }

}
