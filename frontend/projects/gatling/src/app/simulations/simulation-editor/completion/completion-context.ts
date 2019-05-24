export interface CompletionContext {
  precedingKeywords: string[];
  lastKeyword: string | null;
  parentKeyword: string | null;
  parentIndex: number;
  endsWithDot: boolean;
}
