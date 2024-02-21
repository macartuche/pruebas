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

describe('Pais e2e test', () => {
  const paisPageUrl = '/pais';
  const paisPageUrlPattern = new RegExp('/pais(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paisSample = {};

  let pais;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pais+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pais').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pais/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pais) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pais/${pais.id}`,
      }).then(() => {
        pais = undefined;
      });
    }
  });

  it('Pais menu should load Pais page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pais');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Pais').should('exist');
    cy.url().should('match', paisPageUrlPattern);
  });

  describe('Pais page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paisPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Pais page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pais/new$'));
        cy.getEntityCreateUpdateHeading('Pais');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paisPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pais',
          body: paisSample,
        }).then(({ body }) => {
          pais = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pais+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pais?page=0&size=20>; rel="last",<http://localhost/api/pais?page=0&size=20>; rel="first"',
              },
              body: [pais],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paisPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Pais page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pais');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paisPageUrlPattern);
      });

      it('edit button click should load edit Pais page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pais');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paisPageUrlPattern);
      });

      it('edit button click should load edit Pais page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pais');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paisPageUrlPattern);
      });

      it('last delete button click should delete instance of Pais', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pais').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paisPageUrlPattern);

        pais = undefined;
      });
    });
  });

  describe('new Pais page', () => {
    beforeEach(() => {
      cy.visit(`${paisPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Pais');
    });

    it('should create an instance of Pais', () => {
      cy.get(`[data-cy="nombre"]`).type('silkworm ex-husband unto');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'silkworm ex-husband unto');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        pais = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paisPageUrlPattern);
    });
  });
});
