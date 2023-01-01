import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class GlobalService {

  errorMessage: string | null;
  authToken: string | null;

  constructor() {
    this.errorMessage = null;
    this.authToken = null;
  }

  getErrorMessage(): string | null {
    return this.errorMessage;
  }

  setErrorMessage(errorMessage: string | null): void {
    this.errorMessage = errorMessage;
  }

  getAuthToken(): string | null {
    return this.authToken;
  }

  setAuthToken(authToken: string): void {
    this.authToken = authToken;
  }

}
