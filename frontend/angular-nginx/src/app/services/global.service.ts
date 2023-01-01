import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class GlobalService {

  private errorMessage: string | null;
  private authToken: string | null;

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

  setAuthToken(authToken: string | null): void {
    this.authToken = authToken;
  }

  isLoggedIn(): boolean {
    return this.authToken != null;
  }

  logout(): void {
    this.authToken = null;
  }

}
