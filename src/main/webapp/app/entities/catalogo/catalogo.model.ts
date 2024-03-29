export interface ICatalogo {
  id: number;
  codigo?: string | null;
  descripcion?: string | null;
  nuevocampo?: string | null;
  valor?: number | null;
  activo?: boolean | null;
  observacion?: string | null;
}

export type NewCatalogo = Omit<ICatalogo, 'id'> & { id: null };
