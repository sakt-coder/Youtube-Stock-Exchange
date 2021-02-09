import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { tap } from 'rxjs/operators';

@Injectable()
export class HttpConfigInterceptor implements HttpInterceptor {
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        if(localStorage.getItem('Token') != null)
            req = req.clone({headers: req.headers.set('Authorization', localStorage.getItem('Token'))});
        
        return next.handle(req).pipe(
            tap(
                (event: HttpEvent<any>) => {
                    if(event instanceof HttpResponse)
                    {
                        console.log('Response received');
                        if(event.headers.has('Token'))
                        {
                            localStorage.setItem('Token', event.headers.get('Token'));
                            console.log('token set as '+localStorage.getItem('Token'));
                        }
                    }
                    return event;
                },
                (err: any) => {
                    if(err instanceof HttpErrorResponse)
                        if(err.status == 401)
                            console.log("login failed");
                }
        ));
    }

}