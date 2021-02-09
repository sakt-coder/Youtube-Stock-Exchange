import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Channel } from '../models/channel';

@Component({
  selector: 'app-search-stock',
  templateUrl: './search-stock.component.html',
  styleUrls: ['./search-stock.component.css']
})
export class SearchStockComponent implements OnInit {

  query: string;
  results: Channel[];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  search() {
    this.http.get(`${environment.url}/searchChannel?query=${this.query}`)
      .subscribe(
        (data: Channel[]) => {
            this.results = data;
         }
      );
  }

  buy(channel: Channel) {
    let body = new HttpParams();
    body = body.set('channelId', channel.channelId);
    this.http.post('http://localhost:8080/buyShare',body)
      .subscribe(
        (data) => {
            console.log(data);
         }
      );
  }
}
