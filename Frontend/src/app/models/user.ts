import { Holding } from "./holding";

export interface User {
    username: string;
    holdings: Holding[];
    availCash: number;
    netWorth: number;
}