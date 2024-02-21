import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Catalogo e2e test', () => {
  const catalogoPageUrl = '/catalogo';
  const catalogoPageUrlPattern = new RegExp('/catalogo(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const catalogoSample = { codigo: 'why duh anxiously' };

  let catalogo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/catalogos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/catalogos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/catalogos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (catalogo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/catalogos/${catalogo.id}`,
      }).then(() => {
        catalogo = undefined;
      });
    }
  });

  it('Catalogos menu should load Catalogos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalogo');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Catalogo').should('exist');
    cy.url().should('match', catalogoPageUrlPattern);
  });

  describe('Catalogo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(catalogoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Catalogo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/catalogo/new$'));
        cy.getEntityCreateUpdateHeading('Catalogo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/catalogos',
          body: catalogoSample,
        }).then(({ body }) => {
          catalogo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/catalogos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/catalogos?page=0&size=20>; rel="last",<http://localhost/api/catalogos?page=0&size=20>; rel="first"',
              },
              body: [catalogo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(catalogoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Catalogo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('catalogo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogoPageUrlPattern);
      });

      it('edit button click should load edit Catalogo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Catalogo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogoPageUrlPattern);
      });

      it('edit button click should load edit Catalogo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Catalogo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogoPageUrlPattern);
      });

      it('last delete button click should delete instance of Catalogo', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('catalogo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogoPageUrlPattern);

        catalogo = undefined;
      });
    });
  });

  describe('new Catalogo page', () => {
    beforeEach(() => {
      cy.visit(`${catalogoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Catalogo');
    });

    it('should create an instance of Catalogo', () => {
      cy.get(`[data-cy="codigo"]`).type('tool for lest');
      cy.get(`[data-cy="codigo"]`).should('have.value', 'tool for lest');

      cy.get(`[data-cy="descripcion"]`).type('far whether');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'far whether');

      cy.get(`[data-cy="nuevocampo"]`).type('yum yummy beside');
      cy.get(`[data-cy="nuevocampo"]`).should('have.value', 'yum yummy beside');

      cy.get(`[data-cy="valor"]`).type('272.02');
      cy.get(`[data-cy="valor"]`).should('have.value', '272.02');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(`[data-cy="observacion"]`).type('fooey');
      cy.get(`[data-cy="observacion"]`).should('have.value', 'fooey');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        catalogo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', catalogoPageUrlPattern);
    });
  });
});
