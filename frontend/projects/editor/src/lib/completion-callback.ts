export type CompletionCallback = (error: string | null, results: {name: string, value: string, score: number, meta: string}[]) => void;
